export default {
  "cn": "缺陷跟踪平台",
  "en": "BUG TRACKER",
  "subtitle": "把项目缺陷、复现步骤和修复状态整理在同一工作台",
  "accent": "#9d5b88",
  "soft": "#fbedf5",
  "entities": [
    {
      "key": "projects",
      "title": "项目档案",
      "singular": "项目档案",
      "icon": "▣",
      "fields": [
        [
          "code",
          "项目编号",
          "text",
          1
        ],
        [
          "name",
          "项目名称",
          "text",
          1
        ],
        [
          "owner",
          "负责人",
          "text",
          1
        ],
        [
          "status",
          "项目状态",
          "select",
          1,
          [
            "进行中",
            "已归档"
          ]
        ]
      ],
      "columns": [
        "code",
        "name",
        "owner",
        "status"
      ]
    },
    {
      "key": "defects",
      "title": "缺陷记录",
      "singular": "缺陷记录",
      "icon": "◇",
      "fields": [
        [
          "projectId",
          "关联项目",
          "relation",
          1,
          "projects"
        ],
        [
          "issueNo",
          "缺陷编号",
          "text",
          1
        ],
        [
          "title",
          "问题标题",
          "text",
          1
        ],
        [
          "severity",
          "严重程度",
          "select",
          1,
          [
            "低",
            "中",
            "高",
            "致命"
          ]
        ],
        [
          "status",
          "缺陷状态",
          "select",
          1,
          [
            "待确认",
            "待修复",
            "修复中",
            "待验证",
            "已关闭"
          ]
        ],
        [
          "reporter",
          "报告人",
          "text",
          1
        ],
        [
          "eventDate",
          "发现日期",
          "date",
          1
        ],
        [
          "steps",
          "复现步骤",
          "textarea",
          0
        ]
      ],
      "columns": [
        "projectId",
        "issueNo",
        "title",
        "severity",
        "status",
        "reporter",
        "eventDate"
      ]
    }
  ]
}
