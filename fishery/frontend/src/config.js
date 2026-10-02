export default {
  "cn": "水产养殖",
  "en": "AQUA NOTES",
  "subtitle": "池塘与投喂巡查记录，让养殖信息有序留存",
  "accent": "#278ca3",
  "soft": "#e6f7fa",
  "entities": [
    {
      "key": "ponds",
      "title": "养殖池塘",
      "singular": "养殖池塘",
      "icon": "▦",
      "fields": [
        [
          "code",
          "池塘编号",
          "text",
          1
        ],
        [
          "species",
          "养殖品种",
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
          "status",
          "池塘状态",
          "select",
          1,
          [
            "空池",
            "养殖中",
            "休整中"
          ]
        ]
      ],
      "columns": [
        "code",
        "species",
        "area",
        "status"
      ]
    },
    {
      "key": "feedings",
      "title": "投喂记录",
      "singular": "投喂记录",
      "icon": "✦",
      "fields": [
        [
          "pondId",
          "关联池塘",
          "relation",
          1,
          "ponds"
        ],
        [
          "date",
          "记录日期",
          "date",
          1
        ],
        [
          "feed",
          "饲料名称",
          "text",
          1
        ],
        [
          "quantity",
          "投喂量",
          "number",
          1
        ],
        [
          "observer",
          "记录人",
          "text",
          1
        ],
        [
          "notes",
          "巡查备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "pondId",
        "date",
        "feed",
        "quantity",
        "observer"
      ]
    }
  ]
}
