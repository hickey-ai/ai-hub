export default {
  "cn": "宠物照护",
  "en": "PETCARE OS",
  "subtitle": "宠物与照护记录",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "pets",
      "title": "宠物档案",
      "singular": "宠物档案",
      "icon": "▣",
      "fields": [
        [
          "name",
          "宠物档案名称",
          "text",
          1
        ],
        [
          "species",
          "宠物类别",
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
        "species",
        "location"
      ]
    },
    {
      "key": "carelogs",
      "title": "照护记录",
      "singular": "照护记录",
      "icon": "◇",
      "fields": [
        [
          "petId",
          "关联宠物档案",
          "relation",
          1,
          "pets"
        ],
        [
          "caregiver",
          "照护人员",
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
            "待处理",
            "进行中",
            "已完成"
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
        "petId",
        "caregiver",
        "eventDate",
        "status"
      ]
    }
  ]
}
