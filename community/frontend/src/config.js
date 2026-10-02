export default {
  "cn": "社区公益",
  "en": "NEIGHBOR HUB",
  "subtitle": "居民需求与志愿服务，用可追踪的小事连接社区",
  "accent": "#35759d",
  "soft": "#eaf5fc",
  "entities": [
    {
      "key": "programs",
      "title": "服务项目",
      "singular": "服务项目",
      "icon": "▦",
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
          "category",
          "项目类型",
          "select",
          1,
          [
            "便民",
            "志愿",
            "关怀",
            "活动"
          ]
        ],
        [
          "coordinator",
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
            "筹备",
            "开放",
            "已结束"
          ]
        ]
      ],
      "columns": [
        "code",
        "name",
        "category",
        "coordinator",
        "status"
      ]
    },
    {
      "key": "requests",
      "title": "服务需求",
      "singular": "服务需求",
      "icon": "✦",
      "fields": [
        [
          "programId",
          "关联项目",
          "relation",
          1,
          "programs"
        ],
        [
          "title",
          "需求摘要",
          "text",
          1
        ],
        [
          "submittedAt",
          "登记日期",
          "date",
          1
        ],
        [
          "urgency",
          "紧急程度",
          "select",
          1,
          [
            "一般",
            "紧急"
          ]
        ],
        [
          "status",
          "处理状态",
          "select",
          1,
          [
            "待受理",
            "进行中",
            "已完成"
          ]
        ],
        [
          "notes",
          "处理备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "programId",
        "title",
        "submittedAt",
        "urgency",
        "status"
      ]
    }
  ]
}
