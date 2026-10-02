export default {
  "cn": "诊所管理",
  "en": "CLINIC OS",
  "subtitle": "诊室与预约登记",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "clinics",
      "title": "诊室档案",
      "singular": "诊室档案",
      "icon": "▣",
      "fields": [
        [
          "name",
          "诊室档案名称",
          "text",
          1
        ],
        [
          "specialty",
          "服务方向",
          "text",
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
        "specialty",
        "location"
      ]
    },
    {
      "key": "appointments",
      "title": "预约登记",
      "singular": "预约登记",
      "icon": "◇",
      "fields": [
        [
          "clinicId",
          "关联诊室档案",
          "relation",
          1,
          "clinics"
        ],
        [
          "patientAlias",
          "访客代称",
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
            "待到诊",
            "已到诊",
            "已取消"
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
        "clinicId",
        "patientAlias",
        "eventDate",
        "status"
      ]
    }
  ]
}
