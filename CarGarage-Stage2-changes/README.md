# 🚗 Car Garage

**Car Garage** არის ავტომობილების ინვენტარის მართვის Android აპლიკაცია, აგებული
სრულად **Jetpack Compose**-ზე (XML layout-ისა და `findViewById`-ის გარეშე).
აპლიკაცია საშუალებას გაძლევთ დაამატოთ, დაარედაქტიროთ, წაშალოთ და დაათვალიეროთ
ავტოსალონის მანქანები, ხოლო მონაცემები ინახება ლოკალურ **Room** ბაზაში.

---

## 📋 ფუნქციონალი

- **მენიუ (Navigation Drawer)** — გვერდითი მენიუ სამი განყოფილებით: Inventory,
  Statistics და About.
- **ლისთი (LazyColumn)** — ავტომობილების სქროლადი სია ბარათებით, ფასითა და
  სტატუსით (Available / Sold).
- **CRUD** — მანქანის დამატება, რედაქტირება და წაშლა ვალიდაციით.
- **დეტალების ეკრანი** — თითოეული მანქანის სრული ინფორმაცია.
- **სტატისტიკა (charts)** — ინვენტარის ვიზუალიზაცია ხელით დახატული დიაგრამებით
  (იხ. „ახალი ფუნქცია" ქვემოთ).

---

## 🆕 ახალი ფუნქცია — Custom Canvas Charts

პროექტის განმასხვავებელი ფუნქციაა **ხელით დახატული დიაგრამები**, რომლებიც
აგებულია პირდაპირ Compose-ის `Canvas`-ზე, მესამე მხარის ბიბლიოთეკის გარეშე —
ანუ ყველა დიაგრამა ჩვენ თვითონ დავხატეთ:

- **Donut chart** — მანქანების განაწილება საწვავის ტიპის მიხედვით (`drawArc`).
- **Bar chart** — ინვენტარის ღირებულება საწვავის ტიპის მიხედვით (`drawRoundRect`),
  ტექსტის ლეიბლებით native canvas-ზე და შესვლის ანიმაციით.

დიაგრამები რეალურ დროში ეყრდნობა ბაზის მონაცემებს — ახალი მანქანის დამატებისთანავე
სტატისტიკა განახლდება.

---

## 🏛️ არქიტექტურა — MVVM

აპლიკაცია აგებულია სუფთა **MVVM** პრინციპით:

```
View (Compose Screens)
        │  observes StateFlow / calls intents
        ▼
   ViewModel (CarViewModel)
        │  business logic + viewModelScope
        ▼
   Repository (CarRepository)
        │  ერთიანი API
        ▼
   Room (CarDao → CarDatabase)
```

- **View** — Compose ეკრანები (`ui/screens`), მხოლოდ ხატავს UI-ს და აკვირდება
  მდგომარეობას.
- **ViewModel** — ფლობს UI-ის მდგომარეობას (`StateFlow<List<Car>>`) და მონაცემთა
  ცვლილების ერთადერთ წერტილს.
- **Repository** — მალავს Room-ს სუფთა ინტერფეისის მიღმა, რაც შრეებს ერთმანეთისგან
  ხდის დამოუკიდებელს.
- **Room** — ლოკალური SQLite ბაზა, ჭეშმარიტების ერთადერთი წყარო.

---

## 🧰 ტექნიკური სტეკი

| კომპონენტი | ტექნოლოგია |
|---|---|
| ენა | Kotlin |
| UI | Jetpack Compose (Material 3) |
| არქიტექტურა | MVVM (ViewModel + Repository) |
| ბაზა | Room (SQLite) |
| ნავიგაცია | Navigation Compose |
| ასინქრონულობა | Kotlin Coroutines + Flow |
| დიაგრამები | Compose Canvas (ხელით) |
| მინიმალური SDK | API 26 (Android 8.0) |
| Compile SDK | API 35 |

---

## 📁 პროექტის სტრუქტურა

```
app/src/main/java/com/saba/cargarage/
├── MainActivity.kt          # Compose entry point (findViewById-ის გარეშე)
├── CarApplication.kt        # DB + Repository-ის მიმწოდებელი
├── data/                    # Room: Car, CarDao, CarDatabase, Repository, SeedData
├── stats/                   # CarStats — სტატისტიკის სუფთა გამოთვლა
├── ui/
│   ├── CarViewModel.kt      # MVVM ViewModel
│   ├── theme/               # ფერები, ტიპოგრაფია, თემა
│   ├── navigation/          # Routes, CarApp (drawer), CarNavHost
│   ├── components/          # BarChart, DonutChart (Canvas)
│   └── screens/             # List, AddEdit, Detail, About, Statistics
└── util/Format.kt           # ფასისა და გარბენის ფორმატირება
```

---

## ✅ სავალდებულო მოთხოვნების შესრულება

| მოთხოვნა | შესრულება |
|---|---|
| მენიუ | ✅ ModalNavigationDrawer |
| ლისთი | ✅ LazyColumn (Inventory) |
| MVVM არქიტექტურა | ✅ ViewModel + Repository + Room |
| ბაზასთან კავშირი | ✅ Room (RealtimeDatabase/Retrofit-ის ალტერნატივა) |
| ახალი ფუნქცია | ✅ Custom Canvas charts |
| README | ✅ ეს ფაილი |
| `findViewById`-ის გარეშე | ✅ 100% Jetpack Compose |

---

## ▶️ აწყობა და გაშვება

```bash
# Debug APK-ის აწყობა
./gradlew assembleDebug

# ემულატორზე / მოწყობილობაზე ინსტალაცია
./gradlew installDebug
```

პირველ გაშვებაზე ბაზა ავტომატურად ივსება 7 სადემონსტრაციო მანქანით.

---

## 👥 კოლაბორაცია

პროექტი შესრულებულია ორ ეტაპად ორი ავტორის მიერ:

- **Saba Akhvlediani** — საბაზისო აპლიკაცია: Room ბაზა, MVVM, მენიუ, ლისთი და CRUD.
- **Lasha Khutsishvili** — სტატისტიკის ეკრანი და ხელით დახატული დიაგრამები.

პროექტი შესრულდა ორ ეტაპად, ცალ-ცალკე commit-ებით ორივე ავტორისგან.
