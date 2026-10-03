export default {
  "cn": "审计事务所项目",
  "en": "AUDIT WORKPAPER",
  "subtitle": "审计项目与工作底稿登记",
  "accent": "#526a9f",
  "soft": "#edf1fa",
  "entities": [
    {
      "key": "engagements",
      "title": "审计项目",
      "singular": "审计项目",
      "icon": "▤",
      "fields": [
        [
          "code",
          "项目编号",
          "text",
          1
        ],
        [
          "client",
          "客户代称",
          "text",
          1
        ],
        [
          "scope",
          "服务范围",
          "text",
          1
        ],
        [
          "year",
          "会计年度",
          "number",
          1
        ]
      ],
      "columns": [
        "code",
        "client",
        "scope",
        "year"
      ]
    },
    {
      "key": "workpapers",
      "title": "工作底稿",
      "singular": "工作底稿",
      "icon": "▣",
      "fields": [
        [
          "engagementId",
          "关联项目",
          "relation",
          1,
          "engagements"
        ],
        [
          "section",
          "底稿章节",
          "text",
          1
        ],
        [
          "reviewer",
          "复核人",
          "text",
          1
        ],
        [
          "eventDate",
          "记录日期",
          "date",
          1
        ],
        [
          "status",
          "复核状态",
          "select",
          1,
          [
            "草稿",
            "待复核",
            "已复核"
          ]
        ],
        [
          "notes",
          "底稿摘要",
          "textarea",
          0
        ]
      ],
      "columns": [
        "engagementId",
        "section",
        "reviewer",
        "eventDate",
        "status"
      ]
    }
  ]
}
