export default {
  "cn": "房产交易",
  "en": "REALESTATE OS",
  "subtitle": "房源与带看台账",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "listings",
      "title": "房源档案",
      "singular": "房源档案",
      "icon": "▣",
      "fields": [
        [
          "name",
          "房源档案名称",
          "text",
          1
        ],
        [
          "areaSqm",
          "建筑面积（㎡）",
          "number",
          1
        ],
        [
          "location",
          "位置／备注",
          "text",
          1
        ],
        [
          "askingPrice",
          "参考挂牌价（元）",
          "money",
          1
        ]
      ],
      "columns": [
        "name",
        "areaSqm",
        "location",
        "askingPrice"
      ]
    },
    {
      "key": "viewings",
      "title": "带看记录",
      "singular": "带看记录",
      "icon": "◇",
      "fields": [
        [
          "listingId",
          "关联房源档案",
          "relation",
          1,
          "listings"
        ],
        [
          "clientAlias",
          "客户代称",
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
            "待带看",
            "已带看",
            "已取消"
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
        "listingId",
        "clientAlias",
        "eventDate",
        "status"
      ]
    }
  ]
}
