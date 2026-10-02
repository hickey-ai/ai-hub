export default {
  "cn": "宠物美容",
  "en": "PETGROOMING OS",
  "subtitle": "服务与预约登记",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "packages",
      "title": "美容服务",
      "singular": "美容服务",
      "icon": "▣",
      "fields": [
        [
          "name",
          "美容服务名称",
          "text",
          1
        ],
        [
          "referencePrice",
          "参考价格",
          "money",
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
        "referencePrice",
        "location"
      ]
    },
    {
      "key": "appointments",
      "title": "美容预约",
      "singular": "美容预约",
      "icon": "◇",
      "fields": [
        [
          "packageId",
          "关联美容服务",
          "relation",
          1,
          "packages"
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
            "待服务",
            "服务中",
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
        "packageId",
        "petAlias",
        "eventDate",
        "status"
      ]
    }
  ]
}
