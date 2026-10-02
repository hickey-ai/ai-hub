export default {
  "cn": "进销存 ERP",
  "en": "SUPPLY OS",
  "subtitle": "把采购、库存与销售接成一条清晰的业务线",
  "accent": "#5365d9",
  "soft": "#edefff",
  "entities": [
    {
      "key": "products",
      "title": "商品档案",
      "singular": "商品档案",
      "icon": "▣",
      "fields": [
        [
          "sku",
          "商品编码",
          "text",
          1
        ],
        [
          "name",
          "商品名称",
          "text",
          1
        ],
        [
          "unit",
          "单位",
          "text",
          1
        ],
        [
          "stock",
          "库存数量",
          "number",
          1
        ],
        [
          "price",
          "参考单价",
          "money",
          1
        ]
      ],
      "columns": [
        "sku",
        "name",
        "unit",
        "stock",
        "price"
      ]
    },
    {
      "key": "movements",
      "title": "出入库流水",
      "singular": "出入库流水",
      "icon": "↔",
      "fields": [
        [
          "productId",
          "关联商品",
          "relation",
          1,
          "products"
        ],
        [
          "type",
          "业务类型",
          "select",
          1,
          [
            "采购入库",
            "销售出库",
            "库存调整"
          ]
        ],
        [
          "quantity",
          "数量",
          "number",
          1
        ],
        [
          "date",
          "发生日期",
          "date",
          1
        ],
        [
          "counterparty",
          "供应商／客户",
          "text",
          1
        ],
        [
          "notes",
          "业务备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "productId",
        "type",
        "quantity",
        "date",
        "counterparty"
      ]
    }
  ]
}
