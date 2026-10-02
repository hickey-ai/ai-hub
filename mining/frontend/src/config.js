export default {
  "cn": "矿山作业",
  "en": "MINE LOG",
  "subtitle": "作业区域与班次日志，记录演示信息不替代安全生产系统",
  "accent": "#a47443",
  "soft": "#fff1e5",
  "entities": [
    {
      "key": "sites",
      "title": "作业区域",
      "singular": "作业区域",
      "icon": "▦",
      "fields": [
        [
          "code",
          "区域编号",
          "text",
          1
        ],
        [
          "name",
          "区域名称",
          "text",
          1
        ],
        [
          "supervisor",
          "现场负责人",
          "text",
          1
        ],
        [
          "status",
          "区域状态",
          "select",
          1,
          [
            "待检查",
            "作业中",
            "暂停"
          ]
        ]
      ],
      "columns": [
        "code",
        "name",
        "supervisor",
        "status"
      ]
    },
    {
      "key": "shifts",
      "title": "班次记录",
      "singular": "班次记录",
      "icon": "✦",
      "fields": [
        [
          "siteId",
          "关联区域",
          "relation",
          1,
          "sites"
        ],
        [
          "date",
          "作业日期",
          "date",
          1
        ],
        [
          "team",
          "作业班组",
          "text",
          1
        ],
        [
          "headcount",
          "出勤人数",
          "number",
          1
        ],
        [
          "risk",
          "风险记录",
          "select",
          1,
          [
            "待检查",
            "无异常",
            "需整改"
          ]
        ],
        [
          "notes",
          "班次备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "siteId",
        "date",
        "team",
        "headcount",
        "risk"
      ]
    }
  ]
}
