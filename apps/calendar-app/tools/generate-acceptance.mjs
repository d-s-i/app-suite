import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { execFileSync } from 'node:child_process';

// Consume the existing APS parser IR. This is not a Gherkin parser.
export function expandScenarios(ir) {
  const seen = new Set();
  return ir.scenarios.flatMap(scenario => {
    const id = scenario.name.match(/^CAL-001-S\d+/)?.[0];
    if (!id || seen.has(id)) throw new Error(`Duplicate or missing scenario ID: ${scenario.name}`);
    seen.add(id);
    const steps = [...ir.background, ...scenario.steps];
    const placeholders = new Set(steps.flatMap(s => [...s.text.matchAll(/<([^>]+)>/g)].map(m => m[1])));
    return (scenario.examples.length ? scenario.examples : [{}]).map((row, index) => {
      for (const name of placeholders) if (!(name in row)) throw new Error(`Missing parameter ${name}`);
      for (const name of Object.keys(row)) if (!placeholders.has(name)) throw new Error(`Unused parameter ${name}`);
      return { id, row: index + 1, steps: steps.map(s => ({ ...s, text: s.text.replace(/<([^>]+)>/g, (_, key) => row[key]) })) };
    });
  });
}
const quote = text => JSON.stringify(text).replaceAll('$', '\\$');
export function generateKotlin(ir) {
  const featureId = ir.name.match(/^CAL-001-F\d+/)?.[0];
  if (!featureId) throw new Error('Missing stable feature ID');
  const methods = expandScenarios(ir).map(c => `    @Test fun ${c.id.replaceAll('-', '')}_row${c.row}() = runScenario(${quote(c.id)}, listOf(\n${c.steps.map(s => `        ${quote(s.keyword)} to ${quote(s.text)}`).join(',\n')}\n    ))`).join('\n\n');
  return `// Generated from approved Gherkin via parse-gherkin; do not edit.\npackage app.local.calendar.acceptance\n\nimport org.junit.Test\n\nclass ${featureId.replaceAll('-', '')}AcceptanceTest : CalendarSteps() {\n${methods}\n}\n`;
}
function main() {
  const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '../../..');
  const task = path.join(root, '.ai/tasks/CAL-001');
  const output = path.join(task, 'acceptance/generated');
  fs.mkdirSync(output, { recursive: true });
  for (const file of fs.readdirSync(output).filter(f => /^CAL001F\d+AcceptanceTest\.kt$/.test(f))) fs.unlinkSync(path.join(output, file));
  for (const file of fs.readdirSync(path.join(task, 'features')).filter(f => f.endsWith('.feature')).sort()) {
    const irPath = path.join(task, 'acceptance', file.replace('.feature', '.json'));
    execFileSync('parse-gherkin', [path.join(task, 'features', file), irPath], { stdio: 'inherit' });
    const ir = JSON.parse(fs.readFileSync(irPath, 'utf8'));
    const featureId = ir.name.match(/^CAL-001-F\d+/)?.[0];
    const kotlin = generateKotlin(ir);
    fs.writeFileSync(path.join(output, `${featureId.replaceAll('-', '')}AcceptanceTest.kt`), kotlin);
    console.log(`${featureId}: generated ${expandScenarios(ir).length} executable cases`);
  }
}
if (process.argv[1] && path.resolve(process.argv[1]) === fileURLToPath(import.meta.url)) main();
