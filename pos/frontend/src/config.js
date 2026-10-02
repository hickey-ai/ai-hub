export default {
  "cn": "门店收银",
  "en": "POS OS",
  "subtitle": "收银台与手工交易台账",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "counters",
      "title": "收银台档案",
      "singular": "收银台档案",
      "icon": "▣",
      "fields": [
        [
          "name",
          "收银台档案名称",
          "text",
          1
        ],
        [
          "counterArea",
          "门店区域",
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
        "counterArea",
        "location"
      ]
    },
    {
      "key": "receipts",
      "title": "收银记录",
      "singular": "收银记录",
      "icon": "◇",
      "fields": [
        [
          "counterId",
          "关联收银台档案",
          "relation",
          1,
          "counters"
        ],
        [
          "amount",
          "登记金额",
          "money",
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
            "待核对",
            "已登记",
            "已作废"
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
        "counterId",
        "amount",
        "eventDate",
        "status"
      ]
    }
  ]
}
