export default {
  "cn": "博物馆藏品档案",
  "en": "COLLECTIONS",
  "subtitle": "藏品目录与借展记录",
  "accent": "#8b6d3c",
  "soft": "#faf3e6",
  "entities": [
    {
      "key": "objects",
      "title": "馆藏档案",
      "singular": "馆藏档案",
      "icon": "▤",
      "fields": [
        [
          "accessionNo",
          "入藏编号",
          "text",
          1
        ],
        [
          "name",
          "藏品名称",
          "text",
          1
        ],
        [
          "period",
          "年代说明",
          "text",
          1
        ],
        [
          "storage",
          "保存位置",
          "text",
          1
        ]
      ],
      "columns": [
        "accessionNo",
        "name",
        "period",
        "storage"
      ]
    },
    {
      "key": "loans",
      "title": "借展登记",
      "singular": "借展登记",
      "icon": "↔",
      "fields": [
        [
          "objectId",
          "关联藏品",
          "relation",
          1,
          "objects"
        ],
        [
          "borrower",
          "借展单位",
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
          "借展状态",
          "select",
          1,
          [
            "待审核",
            "借出中",
            "已归还"
          ]
        ],
        [
          "notes",
          "借展说明",
          "textarea",
          0
        ]
      ],
      "columns": [
        "objectId",
        "borrower",
        "eventDate",
        "status"
      ]
    }
  ]
}
