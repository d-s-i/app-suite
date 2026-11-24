import { test } from 'node:test';
import assert from 'node:assert/strict';
import { expandScenarios, generateKotlin } from './generate-acceptance.mjs';

const ir = {
  name: 'CAL-001-F01 Example',
  background: [{ keyword: 'Given', text: 'the calendar contains no events' }],
  scenarios: [{ name: 'CAL-001-S03 Example', steps: [
    { keyword: 'When', text: 'the owner opens <month>' },
    { keyword: 'Then', text: 'the title is <expected>' },
  ], examples: [{ month: 'February', expected: 'février' }, { month: 'March', expected: 'mars' }] }],
};
test('expands all rows and retains background and stable scenario ID', () => {
  const cases = expandScenarios(ir);
  assert.equal(cases.length, 2);
  assert.equal(cases[0].id, 'CAL-001-S03');
  assert.equal(cases[0].steps[0].text, 'the calendar contains no events');
  assert.equal(cases[1].steps[1].text, 'the owner opens March');
  assert.equal(cases[1].steps[2].text, 'the title is mars');
});
test('fails closed on missing and unused parameters', () => {
  assert.throws(() => expandScenarios({ ...ir, scenarios: [{ ...ir.scenarios[0], examples: [{ month: 'May' }] }] }), /Missing/);
  assert.throws(() => expandScenarios({ ...ir, scenarios: [{ ...ir.scenarios[0], examples: [{ month: 'May', expected: 'mai', unused: 'x' }] }] }), /Unused/);
});
test('generates executable tests with safely escaped Kotlin strings', () => {
  const source = generateKotlin({ ...ir, background: [{ keyword: 'Given', text: 'a "quote" and $value' }] });
  assert.match(source, /class CAL001F01AcceptanceTest/);
  assert.match(source, /@Test fun CAL001S03_row1/);
  assert.match(source, /@Test fun CAL001S03_row2/);
  assert(source.includes('\\"quote\\" and \\$value'));
  assert(source.includes('runScenario('));
});
test('rejects duplicate scenario IDs', () => {
  assert.throws(() => expandScenarios({ ...ir, scenarios: [ir.scenarios[0], ir.scenarios[0]] }), /Duplicate/);
});
