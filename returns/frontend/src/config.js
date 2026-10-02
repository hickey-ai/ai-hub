export default {
  "cn": "退换货",
  "en": "RETURNS OS",
  "subtitle": "商品与退换登记",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "products",
      "title": "商品档案",
      "singular": "商品档案",
      "icon": "▣",
      "fields": [
        [
          "name",
          "商品档案名称",
          "text",
          1
        ],
        [
          "sku",
          "商品编码",
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
        "sku",
        "location"
      ]
    },
    {
      "key": "requests",
      "title": "退换申请",
      "singular": "退换申请",
      "icon": "◇",
      "fields": [
        [
          "productId",
          "关联商品档案",
          "relation",
          1,
          "products"
        ],
        [
          "reason",
          "申请原因",
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
            "待审核",
            "处理中",
            "已结束"
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
        "reason",
        "eventDate",
        "status"
      ]
    }
  ]
}
