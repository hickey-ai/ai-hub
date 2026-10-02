export default {
  "cn": "宠物诊所",
  "en": "VETERINARY OS",
  "subtitle": "诊室与到访台账",
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
          "focus",
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
        "focus",
        "location"
      ]
    },
    {
      "key": "visits",
      "title": "到访记录",
      "singular": "到访记录",
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
          "petAlias",
          "宠物昵称",
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
            "已预约",
            "已到访",
            "已结束"
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
        "petAlias",
        "eventDate",
        "status"
      ]
    }
  ]
}
