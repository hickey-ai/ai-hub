export default {
  "cn": "合同管理",
  "en": "CONTRACTS OS",
  "subtitle": "模板与合同台账",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "templates",
      "title": "合同模板",
      "singular": "合同模板",
      "icon": "▣",
      "fields": [
        [
          "name",
          "合同模板名称",
          "text",
          1
        ],
        [
          "version",
          "模板版本",
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
        "version",
        "location"
      ]
    },
    {
      "key": "contracts",
      "title": "合同登记",
      "singular": "合同登记",
      "icon": "◇",
      "fields": [
        [
          "templateId",
          "关联合同模板",
          "relation",
          1,
          "templates"
        ],
        [
          "counterparty",
          "相对方",
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
            "草拟",
            "待签署",
            "已归档"
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
        "templateId",
        "counterparty",
        "eventDate",
        "status"
      ]
    }
  ]
}
