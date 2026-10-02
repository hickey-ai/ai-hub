export default {
  "cn": "跨境业务",
  "en": "CROSSBORDER OS",
  "subtitle": "商品与跨境运单",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "products",
      "title": "跨境商品",
      "singular": "跨境商品",
      "icon": "▣",
      "fields": [
        [
          "name",
          "跨境商品名称",
          "text",
          1
        ],
        [
          "origin",
          "原产地区",
          "text",
          1
        ],
        [
          "location",
          "位置／备注",
          "text",
          1
        ]
      ],
      "columns": [
        "name",
        "origin",
        "location"
      ]
    },
    {
      "key": "shipments",
      "title": "跨境运单",
      "singular": "跨境运单",
      "icon": "◇",
      "fields": [
        [
          "productId",
          "关联跨境商品",
          "relation",
          1,
          "products"
        ],
        [
          "destination",
          "目的地区",
          "text",
          1
        ],
        [
          "eventDate",
          "登记日期",
          "date",
          1
        ],
        [
          "status",
          "状态",
          "select",
          1,
          [
            "待准备",
            "运输中",
            "已签收"
          ]
        ],
        [
          "notes",
          "备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "productId",
        "destination",
        "eventDate",
        "status"
      ]
    }
  ]
}
