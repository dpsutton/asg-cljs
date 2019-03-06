(ns asg.sections.slack-signup
  (:require [cljs-styled-components.reagent :refer [defstyled clj-props]]
            [reagent.core :as r]
            [asg.styles.alignment :as align]
            [asg.styles.typography :as typo]
            [clojure.string :as str]))

(def email-regex (re-pattern "^[-!#$%&'*+\\/0-9=?A-Z^_a-z{|}~](\\.?[-!#$%&'*+\\/0-9=?A-Z^_a-z`{|}~])*@[a-zA-Z0-9](-*\\.?[a-zA-Z0-9])*\\.[a-zA-Z](-?[a-zA-Z0-9])+$"))

(def valid-email?
  (every-pred identity
              #(< (count %) 254)
              #(re-matches email-regex %)
              #(< 0 (str/index-of % "@") 64)
              #(let [parts         (str/split % ".")
                     short-enough? (fn [s] (< (count s) 63))]
                 (every? short-enough? parts))))

(defn input [state on-change]
  (let []
    [:input {:style     {:border-color (if (or (str/blank? (:email @state))
                                               (:submittable? @state))
                                         "grey"
                                         "red")}
             :value     (:email @state)
             :type      "text"
             :on-change #(on-change (.. % -target -value))}]))

(defn submit [state]
  [:button {:disabled (not (:submittable? @state))
            :type     "submit"}
   "Submit"])

(def signup-url "https://wnephqc0h5.execute-api.us-east-1.amazonaws.com/prod/slack")

(def state  (r/atom {:submittable? false
                     :stage        :editing
                     :email        ""}))

(defn Signup []
  (let [change (fn [email]
                 (swap! state merge {:email        email
                                     :submittable? (valid-email? email)}))
        action (fn [email]
                 (swap! state assoc :stage :sending)
                 ;; fetch says this request is not OK. Not sure why
                 (-> (js/fetch signup-url
                               #js {:method "POST"
                                    :mode "no-cors"
                                    :cache "no-cache"
                                    :credentials "same-origin"
                                    ;; todo: not sure these headers
                                    ;; are set correctly
                                    ;; https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch
                                    :headers #js {"Content-Type" "application/json"}
                                    :body (.stringify js/JSON #js{:email email})})
                     (.then (fn [response]
                              (swap! state assoc :stage :sent)))
                     (.catch (fn [response]
                               (swap! state assoc :stage :error)))))]
    [align/Container
     [typo/section-header "Slack Signup"]
     (case (:stage @state)
       :editing
       [:form {:on-submit #(do (.preventDefault %)
                               (when (:submittable? @state)
                                 (-> @state :email action)))}
        [input state change]
        [submit state]]
       :sending
       [:h1 "sending state"]
       :sent
       [:h1 "accepted state"]
       :error
       [:h1 "error state"])]))
