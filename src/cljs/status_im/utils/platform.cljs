(ns status-im.utils.platform)

(def platform
  "ios")

(def android? (= platform "android"))
(def ios? (= platform "ios"))

(def platform-specific
  (cond
    android? {}
    ios? {}
    :else {}))