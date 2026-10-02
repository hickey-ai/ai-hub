export default {
  "cn": "图书馆",
  "en": "LIBRARY OS",
  "subtitle": "图书与借阅台账",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "books",
      "title": "馆藏图书",
      "singular": "馆藏图书",
      "icon": "▣",
      "fields": [
        [
          "name",
          "馆藏图书名称",
          "text",
          1
        ],
        [
          "isbn",
          "馆藏编号",
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
        "isbn",
        "location"
      ]
    },
    {
      "key": "loans",
      "title": "借阅登记",
      "singular": "借阅登记",
      "icon": "◇",
      "fields": [
        [
          "bookId",
          "关联馆藏图书",
          "relation",
          1,
          "books"
        ],
        [
          "readerAlias",
          "读者代称",
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
            "借出",
            "已归还",
            "逾期标记"
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
        "bookId",
        "readerAlias",
        "eventDate",
        "status"
      ]
    }
  ]
}
