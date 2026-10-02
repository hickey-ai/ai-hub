export default {
  "cn": "物品租赁",
  "en": "RENTAL STUDIO",
  "subtitle": "设备档案和租借记录，从交付到归还清晰可查",
  "accent": "#b26b4c",
  "soft": "#fff1e9",
  "entities": [
    {
      "key": "assets",
      "title": "出租设备",
      "singular": "出租设备",
      "icon": "▣",
      "fields": [
        [
          "code",
          "设备编号",
          "text",
          1
        ],
        [
          "name",
          "设备名称",
          "text",
          1
        ],
        [
          "category",
          "设备类别",
          "text",
          1
        ],
        [
          "dailyRate",
          "参考日租金",
          "money",
          1
        ],
        [
          "status",
          "设备状态",
          "select",
          1,
          [
            "可租",
            "维护中",
            "停用"
          ]
        ]
      ],
      "columns": [
        "code",
        "name",
        "category",
        "dailyRate",
        "status"
      ]
    },
    {
      "key": "rentals",
      "title": "租借记录",
      "singular": "租借记录",
      "icon": "✦",
      "fields": [
        [
          "assetId",
          "关联设备",
          "relation",
          1,
          "assets"
        ],
        [
          "orderNo",
          "租借单号",
          "text",
          1
        ],
        [
          "renter",
          "租客代号",
          "text",
          1
        ],
        [
          "startDate",
          "起租日期",
          "date",
          1
        ],
        [
          "returnDate",
          "预计归还",
          "date",
          1
        ],
        [
          "status",
          "租借状态",
          "select",
          1,
          [
            "已预约",
            "已借出",
            "已归还",
            "已取消"
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
        "assetId",
        "orderNo",
        "renter",
        "startDate",
        "returnDate",
        "status"
      ]
    }
  ]
}
