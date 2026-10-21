import { test } from 'node:test'
import assert from 'node:assert/strict'
import { filterCustomers } from '../src/customers.js'

test('finds customers across fields, trims queries and leaves records unchanged', () => {
  const customers = [
    Object.freeze({ id: 1, name: 'Acme Studio', contact: 'hello@example.test', industry: 'Software', owner: 'Alex' }),
    Object.freeze({ id: 2, name: '青禾设计', contact: '林女士', industry: '建筑设计', owner: 'Jordan' }),
    Object.freeze({ id: 3, name: 'Other customer' }),
  ]
  Object.freeze(customers)
  for (const [query, ids] of [
    ['  aCmE  ', [1]], ['EXAMPLE.TEST', [1]], ['software', [1]], ['alex', [1]],
    ['青禾', [2]], ['林女士', [2]], ['建筑', [2]], ['JORDAN', [2]],
    ['missing', []], ['', [1, 2, 3]], ['   ', [1, 2, 3]],
  ]) {
    assert.deepEqual(filterCustomers(customers, query).map(customer => customer.id), ids, query)
  }
  assert.equal(customers[0].name, 'Acme Studio')
  assert.equal(customers.length, 3)
})
