export default {
  "cn": "批发采购",
  "en": "TRADE DESK",
  "subtitle": "供应商与采购单的轻量协作台账",
  "accent": "#4373a5",
  "soft": "#edf4fc",
  "entities": [
    {
      "key": "suppliers",
      "title": "供应商档案",
      "singular": "供应商档案",
      "icon": "▣",
      "fields": [
        [
          "code",
          "供应商编号",
          "text",
          1
        ],
        [
          "name",
          "供应商名称",
          "text",
          1
        ],
        [
          "contact",
          "联系岗位",
          "text",
          1
        ],
        [
          "category",
          "供货类别",
          "text",
          1
        ],
        [
          "status",
          "合作状态",
          "select",
          1,
          [
            "待评估",
            "合作中",
            "暂停"
          ]
        ]
      ],
      "columns": [
        "code",
        "name",
        "contact",
        "category",
        "status"
      ]
    },
    {
      "key": "purchases",
      "title": "采购订单",
      "singular": "采购订单",
      "icon": "▤",
      "fields": [
        [
          "supplierId",
          "关联供应商",
          "relation",
          1,
          "suppliers"
        ],
        [
          "orderNo",
          "采购单号",
          "text",
          1
        ],
        [
          "item",
          "采购商品",
          "text",
          1
        ],
        [
          "quantity",
          "数量",
          "number",
          1
        ],
        [
          "unitPrice",
          "参考单价",
          "money",
          1
        ],
        [
          "dueDate",
          "预计到货",
          "date",
          1
        ],
        [
          "status",
          "订单状态",
          "select",
          1,
          [
            "草稿",
            "已确认",
            "已收货",
            "已取消"
          ]
        ]
      ],
      "columns": [
        "supplierId",
        "orderNo",
        "item",
        "quantity",
        "unitPrice",
        "dueDate",
        "status"
      ]
    }
  ]
}
