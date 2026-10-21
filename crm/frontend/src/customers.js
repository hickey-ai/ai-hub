export function filterCustomers(customers, query) {
  const needle = query.trim().toLocaleLowerCase()
  if (!needle) return customers
  return customers.filter(customer =>
    [customer.name, customer.contact, customer.industry, customer.owner]
      .some(value => String(value ?? '').toLocaleLowerCase().includes(needle)))
}
