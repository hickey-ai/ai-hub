export default {
  "cn": "通信设施",
  "en": "SIGNAL DESK",
  "subtitle": "站点与维护任务，掌握网络设施运行记录",
  "accent": "#3367b4",
  "soft": "#edf2ff",
  "entities": [
    {
      "key": "sites",
      "title": "通信站点",
      "singular": "通信站点",
      "icon": "▦",
      "fields": [
        [
          "code",
          "站点编号",
          "text",
          1
        ],
        [
          "name",
          "站点名称",
          "text",
          1
        ],
        [
          "district",
          "所在片区",
          "text",
          1
        ],
        [
          "status",
          "站点状态",
          "select",
          1,
          [
            "运行中",
            "维护中",
            "停用"
          ]
        ]
      ],
      "columns": [
        "code",
        "name",
        "district",
        "status"
      ]
    },
    {
      "key": "workorders",
      "title": "维护任务",
      "singular": "维护任务",
      "icon": "✦",
      "fields": [
        [
          "siteId",
          "关联站点",
          "relation",
          1,
          "sites"
        ],
        [
          "title",
          "维护事项",
          "text",
          1
        ],
        [
          "date",
          "计划日期",
          "date",
          1
        ],
        [
          "priority",
          "优先级",
          "select",
          1,
          [
            "一般",
            "紧急"
          ]
        ],
        [
          "status",
          "任务状态",
          "select",
          1,
          [
            "待处理",
            "处理中",
            "已完成"
          ]
        ],
        [
          "notes",
          "维护备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "siteId",
        "title",
        "date",
        "priority",
        "status"
      ]
    }
  ]
}
