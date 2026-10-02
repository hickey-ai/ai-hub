export default {
  "cn": "售后工单",
  "en": "SERVICE LOOP",
  "subtitle": "从报修、派单到完成，跟踪服务请求的每一步",
  "accent": "#d1664c",
  "soft": "#fff0e9",
  "entities": [
    {
      "key": "customers",
      "title": "客户档案",
      "singular": "客户档案",
      "icon": "▦",
      "fields": [
        [
          "code",
          "客户编号",
          "text",
          1
        ],
        [
          "name",
          "客户名称",
          "text",
          1
        ],
        [
          "contact",
          "联系方式",
          "text",
          1
        ],
        [
          "region",
          "所在区域",
          "text",
          1
        ]
      ],
      "columns": [
        "code",
        "name",
        "contact",
        "region"
      ]
    },
    {
      "key": "tickets",
      "title": "服务工单",
      "singular": "服务工单",
      "icon": "✦",
      "fields": [
        [
          "customerId",
          "关联客户",
          "relation",
          1,
          "customers"
        ],
        [
          "ticketNo",
          "工单编号",
          "text",
          1
        ],
        [
          "subject",
          "服务主题",
          "text",
          1
        ],
        [
          "priority",
          "优先级",
          "select",
          1,
          [
            "普通",
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
            "已派单",
            "处理中",
            "已解决"
          ]
        ],
        [
          "dueDate",
          "期望完成",
          "date",
          1
        ],
        [
          "notes",
          "处理记录",
          "textarea",
          0
        ]
      ],
      "columns": [
        "customerId",
        "ticketNo",
        "subject",
        "priority",
        "status",
        "dueDate"
      ]
    }
  ]
}
