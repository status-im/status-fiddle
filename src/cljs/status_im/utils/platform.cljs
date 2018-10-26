(ns status-im.utils.platform)

(def platform "ios")

(def android? (= platform "android"))
(def ios? (= platform "ios"))
(def desktop? false)
(def iphone-x? false)
(def isMacOs? false)
(def isNix? false)

(def platform-specific
  (cond
    android? {}
    ios? {}
    :else {}))