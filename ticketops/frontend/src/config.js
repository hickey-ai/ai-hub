export default {
  "cn": "内部工单平台",
  "en": "TICKET DESK",
  "subtitle": "按队列记录需求、指派处理人与工单进度",
  "accent": "#277d9d",
  "soft": "#eaf6fb",
  "entities": [
    {
      "key": "queues",
      "title": "工单队列",
      "singular": "工单队列",
      "icon": "▦",
      "fields": [
        [
          "code",
          "队列编号",
          "text",
          1
        ],
        [
          "name",
          "队列名称",
          "text",
          1
        ],
        [
          "owner",
          "负责团队",
          "text",
          1
        ],
        [
          "status",
          "队列状态",
          "select",
          1,
          [
            "启用",
            "停用"
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
      "key": "tickets",
      "title": "内部工单",
      "singular": "内部工单",
      "icon": "✦",
      "fields": [
        [
          "queueId",
          "所属队列",
          "relation",
          1,
          "queues"
        ],
        [
          "ticketNo",
          "工单编号",
          "text",
          1
        ],
        [
          "title",
          "工单标题",
          "text",
          1
        ],
        [
          "priority",
          "优先级",
          "select",
          1,
          [
            "低",
            "普通",
            "高",
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
            "处理中",
            "待确认",
            "已解决",
            "已关闭"
          ]
        ],
        [
          "assignee",
          "处理人",
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
          "notes",
          "处理备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "queueId",
        "ticketNo",
        "title",
        "priority",
        "status",
        "assignee",
        "eventDate"
      ]
    }
  ]
}
