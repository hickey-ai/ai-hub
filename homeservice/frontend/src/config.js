export default {
  "cn": "上门服务",
  "en": "HOME CREW",
  "subtitle": "客户登记和上门任务，透明记录服务进度",
  "accent": "#bd7461",
  "soft": "#fff1ea",
  "entities": [
    {
      "key": "customers",
      "title": "客户档案",
      "singular": "客户档案",
      "icon": "▣",
      "fields": [
        [
          "code",
          "客户编号",
          "text",
          1
        ],
        [
          "name",
          "客户代号",
          "text",
          1
        ],
        [
          "area",
          "服务区域",
          "text",
          1
        ],
        [
          "requestType",
          "常用服务",
          "text",
          1
        ]
      ],
      "columns": [
        "code",
        "name",
        "area",
        "requestType"
      ]
    },
    {
      "key": "jobs",
      "title": "上门任务",
      "singular": "上门任务",
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
          "title",
          "服务事项",
          "text",
          1
        ],
        [
          "appointmentDate",
          "预约日期",
          "date",
          1
        ],
        [
          "worker",
          "服务人员",
          "text",
          1
        ],
        [
          "status",
          "任务状态",
          "select",
          1,
          [
            "待分配",
            "已预约",
            "服务中",
            "已完成",
            "已取消"
          ]
        ],
        [
          "notes",
          "服务备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "customerId",
        "title",
        "appointmentDate",
        "worker",
        "status"
      ]
    }
  ]
}
