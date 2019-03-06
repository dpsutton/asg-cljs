(ns asg.main
  (:require [reagent.core :as r]
            [asg.styles.alignment :as align]
            [asg.styles.typography :as typo]
            [asg.header :as header]
            [asg.sections.banner :as banner]
            [asg.sections.events :as events]
            [asg.sections.slack-signup :as slack]
            [asg.stripes :as stripes]
            [clojure.string :as str]
            ["typography" :as typography]
            ["react-typography" :as rtypo]))

(defn asg-page []
  [:div
   [rtypo/TypographyStyle #js{:typography (typography (clj->js typo/options))}]
   [rtypo/GoogleFont #js{:typography (typography (clj->js typo/options))}]
   [header/Header]
   [banner/Banner]
   [stripes/MainStripe]
   [events/Events {:max-events 10}]
   [slack/Signup]])

(defn main! []
  (r/render [asg-page]
            (js/document.getElementById "app")))

(defn ^:dev/after-load reload! []
  (main!))
