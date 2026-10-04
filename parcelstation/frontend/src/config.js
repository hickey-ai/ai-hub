export default {
  "cn": "快递驿站",
  "en": "PARCEL STATION",
  "subtitle": "入库、取件码与异常件处理",
  "accent": "#286f98",
  "soft": "#e8f4fa",
  "entities": [
    {
      "key": "shelves",
      "title": "货架档案",
      "singular": "货架档案",
      "icon": "▤",
      "fields": [
        [
          "code",
          "货架编号",
          "text",
          1
        ],
        [
          "name",
          "货架名称",
          "text",
          1
        ],
        [
          "capacity",
          "参考容量",
          "number",
          1
        ],
        [
          "location",
          "存放区域",
          "text",
          1
        ]
      ],
      "columns": [
        "code",
        "name",
        "capacity",
        "location"
      ]
    },
    {
      "key": "parcels",
      "title": "包裹流转",
      "singular": "包裹流转",
      "icon": "↗",
      "fields": [
        [
          "shelfId",
          "存放货架",
          "relation",
          1,
          "shelves"
        ],
        [
          "trackingNo",
          "包裹编号",
          "text",
          1
        ],
        [
          "recipient",
          "收件人代称",
          "text",
          1
        ],
        [
          "pickupCode",
          "取件码",
          "text",
          1
        ],
        [
          "eventDate",
          "入库日期",
          "date",
          1
        ],
        [
          "status",
          "流转状态",
          "select",
          1,
          [
            "待取件",
            "已签收",
            "异常件"
          ]
        ],
        [
          "notes",
          "异常或签收备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "shelfId",
        "trackingNo",
        "recipient",
        "pickupCode",
        "eventDate",
        "status"
      ]
    }
  ]
}
