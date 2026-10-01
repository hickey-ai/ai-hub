export default {
  "cn": "养娃记录",
  "en": "LITTLE DAYS",
  "subtitle": "把成长中的小事，认真珍藏",
  "accent": "#bd6c54",
  "soft": "#fff1e9",
  "entities": [
    {
      "key": "children",
      "title": "成长档案",
      "singular": "档案",
      "icon": "✿",
      "fields": [
        [
          "name",
          "称呼",
          "text",
          true
        ],
        [
          "birthDate",
          "出生日期",
          "date",
          true
        ],
        [
          "notes",
          "备注",
          "textarea",
          false
        ]
      ],
      "columns": [
        "name",
        "birthDate"
      ]
    },
    {
      "key": "moments",
      "title": "成长记录",
      "singular": "记录",
      "icon": "✦",
      "fields": [
        [
          "childId",
          "关联档案",
          "relation",
          true,
          "children"
        ],
        [
          "date",
          "记录日期",
          "date",
          true
        ],
        [
          "category",
          "类别",
          "select",
          true,
          [
            "成长里程碑",
            "日常生活",
            "学习兴趣",
            "健康观察",
            "其他"
          ]
        ],
        [
          "title",
          "记录标题",
          "text",
          true
        ],
        [
          "notes",
          "详情",
          "textarea",
          false
        ]
      ],
      "columns": [
        "childId",
        "date",
        "category",
        "title"
      ]
    }
  ]
}
