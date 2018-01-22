(ns status-im.i18n)

(defn label
  ([path] (label path {}))
  ([path options]
   (name path)))

(defn get-contact-translated [contact-id key fallback]
  "")
