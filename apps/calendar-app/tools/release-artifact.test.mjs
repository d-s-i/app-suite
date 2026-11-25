import { test } from 'node:test';
import assert from 'node:assert/strict';
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { execFileSync } from 'node:child_process';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const release = path.join(root, 'build/outputs/apk/release/calendar-release.apk');
const debug = path.join(root, 'build/outputs/apk/debug/calendar-debug.apk');
const apksigner = path.join(process.env.ANDROID_HOME ?? '', 'build-tools/35.0.0/apksigner');
function certificate(apk) {
  assert.ok(process.env.ANDROID_HOME, 'Run in the Android SDK environment');
  const output = execFileSync(apksigner, ['verify', '--print-certs', apk], { encoding: 'utf8' });
  const hashes = [...output.matchAll(/^Signer #\d+ certificate SHA-256 digest: ([a-f0-9]+)$/gm)];
  assert.equal(hashes.length, 1, 'APK must verify with exactly one signing certificate');
  return hashes[0][1];
}
function manifest(apk, field) {
  return execFileSync('apkanalyzer', ['manifest', field, apk], { encoding: 'utf8' }).trim();
}
function releaseCertificate() {
  const hash = fs.readFileSync(path.join(root, 'release-certificate.sha256'), 'utf8').trim();
  assert.match(hash, /^[a-f0-9]{64}$/);
  return hash;
}
test('release APK verifies against the independently exported public certificate', () => {
  assert.equal(certificate(release), releaseCertificate());
});
test('release is not debuggable and retains the application identity', () => {
  assert.equal(manifest(release, 'debuggable'), 'false');
  assert.equal(manifest(release, 'application-id'), 'app.local.calendar');
  assert.match(manifest(release, 'version-code'), /^[1-9]\d*$/);
});
test('debug retains a distinct development signature', () => {
  assert.equal(manifest(debug, 'debuggable'), 'true');
  assert.equal(manifest(debug, 'application-id'), 'app.local.calendar');
  assert.notEqual(certificate(debug), releaseCertificate());
});
