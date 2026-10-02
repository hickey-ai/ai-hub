export default {
  "cn": "康复训练",
  "en": "REHAB OS",
  "subtitle": "方案与训练记录",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "programs",
      "title": "训练方案",
      "singular": "训练方案",
      "icon": "▣",
      "fields": [
        [
          "name",
          "训练方案名称",
          "text",
          1
        ],
        [
          "sessions",
          "计划次数",
          "number",
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
        "sessions",
        "location"
      ]
    },
    {
      "key": "sessions",
      "title": "训练记录",
      "singular": "训练记录",
      "icon": "◇",
      "fields": [
        [
          "programId",
          "关联训练方案",
          "relation",
          1,
          "programs"
        ],
        [
          "participantAlias",
          "参与者代称",
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
            "待开始",
            "进行中",
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
        "programId",
        "participantAlias",
        "eventDate",
        "status"
      ]
    }
  ]
}
