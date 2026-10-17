// Quote every CSV cell and neutralize spreadsheet formula prefixes, including
// leading whitespace/control characters. Keep currency amounts as CNY values.
export function csvCell(value) {
  let text = String(value ?? '')
  if (/^[\s\u0000-\u001f]*[=+@-]/u.test(text) || /^[\t\r\n]/u.test(text)) text = "'" + text
  return '"' + text.replaceAll('"', '""') + '"'
}

export function opportunitiesCsv(opportunities, customers, translate = text => text) {
  const names = new Map(customers.map(customer => [customer.id, customer.name]))
  const rows = [
    ['编号', '商机名称', '客户编号', '客户名称', '金额', '阶段', '负责人', '创建日期'].map(translate),
    ...opportunities.map(deal => [deal.id, deal.title, deal.customerId,
      names.get(deal.customerId) ?? translate('未知客户'), deal.amount,
      translate(deal.stage), deal.owner, deal.createdAt]),
  ]
  return '\uFEFF' + rows.map(row => row.map(csvCell).join(',')).join('\r\n') + '\r\n'
}
