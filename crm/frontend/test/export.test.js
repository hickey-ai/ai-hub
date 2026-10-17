import { test } from 'node:test'
import assert from 'node:assert/strict'
import { csvCell, opportunitiesCsv } from '../src/export.js'
import { translate } from '../src/language.js'

test('quotes commas, double quotes, multiline and Unicode without losing content', () => {
  assert.equal(csvCell('Acme, "Studio"\n客户'), '"Acme, ""Studio""\n客户"')
})

test('neutralizes spreadsheet formulas including whitespace-prefixed payloads', () => {
  for (const value of ['=1+1', '+cmd', '-1+2', '@SUM(A1)', '  =1', '\t=1', '\r=1', '\n@A1', '\u0000=1']) {
    assert.equal(csvCell(value), `"'${value}"`)
  }
  assert.equal(csvCell(1000), '"1000"')
  assert.equal(csvCell(null), '""')
})

test('exports supplied filtered rows with English stages, stable IDs and CNY values', () => {
  const deals = [{ id: 8, title: '=1+1', customerId: 4, amount: 1250, stage: '商务谈判', owner: 'Alex', createdAt: '2026-10-17' }]
  const csv = opportunitiesCsv(deals, [{ id: 4, name: 'Acme, Inc' }], text => translate(text, 'en'))
  assert.ok(csv.startsWith('\uFEFF"ID","Opportunity","Customer ID","Customer name","Amount (CNY)"'))
  assert.ok(csv.includes('"8","\'=1+1","4","Acme, Inc","1250","Negotiation","Alex","2026-10-17"'))
  assert.equal(csv.split('\r\n').length, 3)
  assert.ok(!opportunitiesCsv([], []).includes('Acme'))
})

test('keeps persisted API stage labels unchanged while translating their presentation', () => {
  for (const stage of ['发现需求', '方案沟通', '商务谈判', '已赢单', '已流失']) {
    assert.notEqual(translate(stage, 'en'), stage)
    assert.equal(translate(stage, 'zh'), stage)
  }
})
