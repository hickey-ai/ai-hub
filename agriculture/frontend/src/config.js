export default {
  "cn": "农业养殖",
  "en": "FIELD NOTES",
  "subtitle": "地块、作物与农事记录，让田间信息不再散落",
  "accent": "#63853c",
  "soft": "#f0f7e7",
  "entities": [
    {
      "key": "plots",
      "title": "地块档案",
      "singular": "地块档案",
      "icon": "▦",
      "fields": [
        [
          "code",
          "地块编号",
          "text",
          1
        ],
        [
          "crop",
          "作物／品种",
          "text",
          1
        ],
        [
          "area",
          "面积（亩）",
          "money",
          1
        ],
        [
          "manager",
          "负责人",
          "text",
          1
        ],
        [
          "status",
          "地块状态",
          "select",
          1,
          [
            "备耕",
            "种植中",
            "已收获"
          ]
        ]
      ],
      "columns": [
        "code",
        "crop",
        "area",
        "manager",
        "status"
      ]
    },
    {
      "key": "activities",
      "title": "农事记录",
      "singular": "农事记录",
      "icon": "✦",
      "fields": [
        [
          "plotId",
          "关联地块",
          "relation",
          1,
          "plots"
        ],
        [
          "date",
          "作业日期",
          "date",
          1
        ],
        [
          "type",
          "作业类型",
          "select",
          1,
          [
            "播种",
            "灌溉",
            "施肥",
            "巡查",
            "收获"
          ]
        ],
        [
          "quantity",
          "投入量",
          "number",
          1
        ],
        [
          "notes",
          "作业详情",
          "textarea",
          0
        ]
      ],
      "columns": [
        "plotId",
        "date",
        "type",
        "quantity"
      ]
    }
  ]
}
