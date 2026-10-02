export default {
  "cn": "法律服务",
  "en": "CASE FILE",
  "subtitle": "委托事项与案件进度集中整理，避免关键节点遗漏",
  "accent": "#554b82",
  "soft": "#f1eef9",
  "entities": [
    {
      "key": "clients",
      "title": "委托人档案",
      "singular": "委托人档案",
      "icon": "▦",
      "fields": [
        [
          "code",
          "委托编号",
          "text",
          1
        ],
        [
          "name",
          "委托人称呼",
          "text",
          1
        ],
        [
          "caseType",
          "业务类型",
          "text",
          1
        ],
        [
          "contact",
          "联系信息",
          "text",
          1
        ]
      ],
      "columns": [
        "code",
        "name",
        "caseType",
        "contact"
      ]
    },
    {
      "key": "cases",
      "title": "案件进度",
      "singular": "案件进度",
      "icon": "✦",
      "fields": [
        [
          "clientId",
          "关联委托人",
          "relation",
          1,
          "clients"
        ],
        [
          "caseNo",
          "案件编号",
          "text",
          1
        ],
        [
          "title",
          "事项名称",
          "text",
          1
        ],
        [
          "openedAt",
          "受理日期",
          "date",
          1
        ],
        [
          "stage",
          "当前阶段",
          "select",
          1,
          [
            "咨询",
            "材料准备",
            "办理中",
            "已结案"
          ]
        ],
        [
          "nextAction",
          "下一步行动",
          "text",
          1
        ],
        [
          "notes",
          "案件备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "clientId",
        "caseNo",
        "title",
        "openedAt",
        "stage",
        "nextAction"
      ]
    }
  ]
}
