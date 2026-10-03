export default {
  "cn": "汽车销售线索",
  "en": "AUTO SHOWROOM",
  "subtitle": "车辆档案与客户意向记录",
  "accent": "#4775b6",
  "soft": "#edf3fb",
  "entities": [
    {
      "key": "listings",
      "title": "在售车辆",
      "singular": "在售车辆",
      "icon": "▣",
      "fields": [
        [
          "vinAlias",
          "车辆编号",
          "text",
          1
        ],
        [
          "model",
          "车型名称",
          "text",
          1
        ],
        [
          "mileage",
          "参考里程",
          "number",
          1
        ],
        [
          "price",
          "参考售价",
          "money",
          1
        ],
        [
          "condition",
          "车辆类别",
          "select",
          1,
          [
            "新车",
            "二手车"
          ]
        ]
      ],
      "columns": [
        "vinAlias",
        "model",
        "mileage",
        "price",
        "condition"
      ]
    },
    {
      "key": "leads",
      "title": "客户意向",
      "singular": "客户意向",
      "icon": "✦",
      "fields": [
        [
          "listingId",
          "关联车辆",
          "relation",
          1,
          "listings"
        ],
        [
          "customer",
          "客户称呼",
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
          "跟进状态",
          "select",
          1,
          [
            "新线索",
            "已联系",
            "已结束"
          ]
        ],
        [
          "notes",
          "跟进说明",
          "textarea",
          0
        ]
      ],
      "columns": [
        "listingId",
        "customer",
        "eventDate",
        "status"
      ]
    }
  ]
}
