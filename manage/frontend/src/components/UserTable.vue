<script setup>
defineProps({ rows: { type: Array, default: () => [] }, compact: Boolean })
defineEmits(['edit', 'remove'])
</script>
<template>
  <div class="table-wrap">
    <table>
      <thead><tr><th>成员</th><th>部门</th><th>角色</th><th>状态</th><th>操作</th></tr></thead>
      <tbody>
        <tr v-for="u in rows" :key="u.id">
          <td><div class="person"><span class="avatar" :class="['avatar-purple','avatar-blue','avatar-orange','avatar-green'][u.id%4]">{{u.name.slice(0,1)}}</span><div><b>{{u.name}}</b><small>{{u.email}}</small></div></div></td>
          <td>{{u.department}}</td><td>{{u.role}}</td>
          <td><span class="status-dot" :class="u.status==='正常'?'good':'off'">● {{u.status}}</span></td>
          <td><button class="row-action" @click="$emit('edit',u)">编辑</button><button v-if="!compact" class="row-action danger" @click="$emit('remove',u)">删除</button></td>
        </tr>
      </tbody>
    </table>
    <div v-if="!rows.length" class="empty">没有找到符合条件的用户。</div>
  </div>
</template>
