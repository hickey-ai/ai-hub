export default {
  "cn": "仓储管理",
  "en": "WAREHOUSE FLOW",
  "subtitle": "库位和库存作业记录，帮助团队看清货物去向",
  "accent": "#2e79ad",
  "soft": "#eaf5fd",
  "entities": [
    {
      "key": "bins",
      "title": "仓库库位",
      "singular": "仓库库位",
      "icon": "▦",
      "fields": [
        [
          "code",
          "库位编号",
          "text",
          1
        ],
        [
          "warehouse",
          "仓库名称",
          "text",
          1
        ],
        [
          "zone",
          "所在区域",
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
          "status",
          "库位状态",
          "select",
          1,
          [
            "可用",
            "停用"
          ]
        ]
      ],
      "columns": [
        "code",
        "warehouse",
        "zone",
        "capacity",
        "status"
      ]
    },
    {
      "key": "movements",
      "title": "作业记录",
      "singular": "作业记录",
      "icon": "↔",
      "fields": [
        [
          "binId",
          "关联库位",
          "relation",
          1,
          "bins"
        ],
        [
          "sku",
          "货品编码",
          "text",
          1
        ],
        [
          "type",
          "作业类型",
          "select",
          1,
          [
            "入库",
            "拣货",
            "盘点",
            "出库"
          ]
        ],
        [
          "quantity",
          "作业数量",
          "number",
          1
        ],
        [
          "date",
          "作业日期",
          "date",
          1
        ],
        [
          "notes",
          "作业备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "binId",
        "sku",
        "type",
        "quantity",
        "date"
      ]
    }
  ]
}
