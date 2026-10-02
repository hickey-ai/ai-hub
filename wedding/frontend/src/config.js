export default {
  "cn": "婚庆策划",
  "en": "WEDDING OS",
  "subtitle": "方案与婚礼档期",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "plans",
      "title": "婚礼方案",
      "singular": "婚礼方案",
      "icon": "▣",
      "fields": [
        [
          "name",
          "婚礼方案名称",
          "text",
          1
        ],
        [
          "budget",
          "参考预算",
          "money",
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
        "budget",
        "location"
      ]
    },
    {
      "key": "events",
      "title": "婚礼档期",
      "singular": "婚礼档期",
      "icon": "◇",
      "fields": [
        [
          "planId",
          "关联婚礼方案",
          "relation",
          1,
          "plans"
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
            "筹备中",
            "执行中",
            "已完成"
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
        "planId",
        "clientAlias",
        "eventDate",
        "status"
      ]
    }
  ]
}
