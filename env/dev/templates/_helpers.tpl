{{- define "registration.namespace" -}}
    {{- required "Missing namespace" .Values.global.namespace -}}
{{- end -}}
