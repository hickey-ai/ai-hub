export default {
  "cn": "公共服务",
  "en": "CIVIC FLOW",
  "subtitle": "服务事项与办理登记，为申请处理提供可见进度",
  "accent": "#44779b",
  "soft": "#eaf4fa",
  "entities": [
    {
      "key": "services",
      "title": "服务事项",
      "singular": "服务事项",
      "icon": "▦",
      "fields": [
        [
          "code",
          "事项编号",
          "text",
          1
        ],
        [
          "name",
          "事项名称",
          "text",
          1
        ],
        [
          "department",
          "承办部门",
          "text",
          1
        ],
        [
          "days",
          "参考办理天数",
          "number",
          1
        ],
        [
          "status",
          "事项状态",
          "select",
          1,
          [
            "开放",
            "暂停"
          ]
        ]
      ],
      "columns": [
        "code",
        "name",
        "department",
        "days",
        "status"
      ]
    },
    {
      "key": "cases",
      "title": "办理登记",
      "singular": "办理登记",
      "icon": "✦",
      "fields": [
        [
          "serviceId",
          "关联事项",
          "relation",
          1,
          "services"
        ],
        [
          "caseNo",
          "登记编号",
          "text",
          1
        ],
        [
          "applicant",
          "申请人代号",
          "text",
          1
        ],
        [
          "date",
          "登记日期",
          "date",
          1
        ],
        [
          "status",
          "处理状态",
          "select",
          1,
          [
            "待受理",
            "处理中",
            "已完成",
            "已退回"
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
        "serviceId",
        "caseNo",
        "applicant",
        "date",
        "status"
      ]
    }
  ]
}
