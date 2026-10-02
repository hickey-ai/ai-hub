export default {
  "cn": "摄影工作室",
  "en": "PHOTOGRAPHY OS",
  "subtitle": "套餐与拍摄预约",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "packages",
      "title": "拍摄套餐",
      "singular": "拍摄套餐",
      "icon": "▣",
      "fields": [
        [
          "name",
          "拍摄套餐名称",
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
      "key": "shoots",
      "title": "拍摄预约",
      "singular": "拍摄预约",
      "icon": "◇",
      "fields": [
        [
          "packageId",
          "关联拍摄套餐",
          "relation",
          1,
          "packages"
        ],
        [
          "clientAlias",
          "客户代称",
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
            "待拍摄",
            "修片中",
            "已交付"
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
        "clientAlias",
        "eventDate",
        "status"
      ]
    }
  ]
}
