export default {
  "cn": "保险服务",
  "en": "POLICY DESK",
  "subtitle": "保单档案与理赔进度，展示服务流程记录",
  "accent": "#4278bd",
  "soft": "#ecf3ff",
  "entities": [
    {
      "key": "policies",
      "title": "保单档案",
      "singular": "保单档案",
      "icon": "▣",
      "fields": [
        [
          "policyNo",
          "保单编号",
          "text",
          1
        ],
        [
          "product",
          "产品类别",
          "text",
          1
        ],
        [
          "holder",
          "投保人代号",
          "text",
          1
        ],
        [
          "effectiveDate",
          "起保日期",
          "date",
          1
        ],
        [
          "status",
          "保单状态",
          "select",
          1,
          [
            "待生效",
            "有效",
            "已终止"
          ]
        ]
      ],
      "columns": [
        "policyNo",
        "product",
        "holder",
        "effectiveDate",
        "status"
      ]
    },
    {
      "key": "claims",
      "title": "理赔进度",
      "singular": "理赔进度",
      "icon": "✦",
      "fields": [
        [
          "policyId",
          "关联保单",
          "relation",
          1,
          "policies"
        ],
        [
          "claimNo",
          "案件编号",
          "text",
          1
        ],
        [
          "reportedDate",
          "报案日期",
          "date",
          1
        ],
        [
          "amount",
          "申报金额",
          "money",
          1
        ],
        [
          "status",
          "案件状态",
          "select",
          1,
          [
            "已报案",
            "资料审核",
            "已结案",
            "已撤回"
          ]
        ],
        [
          "notes",
          "处理备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "policyId",
        "claimNo",
        "reportedDate",
        "amount",
        "status"
      ]
    }
  ]
}
