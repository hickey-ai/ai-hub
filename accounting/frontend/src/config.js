export default {
  "cn": "财务记账",
  "en": "ACCOUNTING OS",
  "subtitle": "账簿与手工记账",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "ledgers",
      "title": "账簿分类",
      "singular": "账簿分类",
      "icon": "▣",
      "fields": [
        [
          "name",
          "账簿分类名称",
          "text",
          1
        ],
        [
          "category",
          "账目类别",
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
        "category",
        "location"
      ]
    },
    {
      "key": "entries",
      "title": "记账条目",
      "singular": "记账条目",
      "icon": "◇",
      "fields": [
        [
          "ledgerId",
          "关联账簿分类",
          "relation",
          1,
          "ledgers"
        ],
        [
          "amount",
          "记账金额",
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
            "已记录",
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
        "ledgerId",
        "amount",
        "eventDate",
        "status"
      ]
    }
  ]
}
