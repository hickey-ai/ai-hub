export default {
  "cn": "采购招投标台账",
  "en": "BID DESK",
  "subtitle": "采购需求与报价记录",
  "accent": "#755fae",
  "soft": "#f2effc",
  "entities": [
    {
      "key": "requests",
      "title": "采购需求",
      "singular": "采购需求",
      "icon": "▤",
      "fields": [
        [
          "requestNo",
          "需求编号",
          "text",
          1
        ],
        [
          "title",
          "需求名称",
          "text",
          1
        ],
        [
          "department",
          "申请部门",
          "text",
          1
        ],
        [
          "budget",
          "参考预算",
          "money",
          1
        ],
        [
          "deadline",
          "截止日期",
          "date",
          1
        ]
      ],
      "columns": [
        "requestNo",
        "title",
        "department",
        "budget",
        "deadline"
      ]
    },
    {
      "key": "bids",
      "title": "报价记录",
      "singular": "报价记录",
      "icon": "◇",
      "fields": [
        [
          "requestId",
          "关联需求",
          "relation",
          1,
          "requests"
        ],
        [
          "vendor",
          "报价单位",
          "text",
          1
        ],
        [
          "amount",
          "报价金额",
          "money",
          1
        ],
        [
          "eventDate",
          "报价日期",
          "date",
          1
        ],
        [
          "status",
          "记录状态",
          "select",
          1,
          [
            "待核对",
            "已核对",
            "已归档"
          ]
        ],
        [
          "notes",
          "报价说明",
          "textarea",
          0
        ]
      ],
      "columns": [
        "requestId",
        "vendor",
        "amount",
        "eventDate",
        "status"
      ]
    }
  ]
}
