# Car Garage

Car Garage არის Android აპლიკაცია ავტოსალონის მანქანების სამართავად. მთლიანად Jetpack Compose-ზეა დაწერილი, ისე რომ არსად გვხვდება XML layout-ები და findViewById. მონაცემები ინახება ლოკალურად, Room-ის ბაზაში.

## რას აკეთებს

გვერდითი მენიუ (Navigation Drawer), სამი განყოფილებით: Inventory, Statistics და About.
მანქანების სია LazyColumn-ით, სადაც ჩანს ფასი და სტატუსი — Available თუ Sold.
მანქანის დამატება, რედაქტირება და წაშლა, ვალიდაციით.
დეტალების გვერდი თითოეულ მანქანაზე.
სტატისტიკის გვერდი დიაგრამებით.

## დიაგრამები

დიაგრამები:

Donut chart — აჩვენებს მანქანების განაწილებას საწვავის ტიპის მიხედვით (drawArc-ით).
Bar chart — აჩვენებს ინვენტარის ღირებულებას საწვავის ტიპების მიხედვით (drawRoundRect-ით), აქვს ტექსტის ლეიბლები და მცირე ანიმაცია გამოჩენისას.

დიაგრამები ცოცხლდება ბაზის მონაცემებზე — როგორც კი ახალ მანქანას დაამატებ, სტატისტიკაც განახლდება.

## არქიტექტურა

ვცადეთ MVVM-ის დაცვა:

View (Compose ეკრანები) → ViewModel (CarViewModel) → Repository (CarRepository) → Room (CarDao / CarDatabase)

View-ში მხოლოდ UI ხატავს და StateFlow-ს აკვირდება.
ViewModel ინახავს UI-ის მდგომარეობას, აქ ხდება ბიზნეს ლოგიკაც.
Repository არის შუალედური ფენა Room-სა და ViewModel-ს შორის.
Room არის ჩვენი ერთადერთი მონაცემთა წყარო.

## რაზეა დაწერილი

| რა | რაში |
|---|---|
| ენა | Kotlin |
| UI | Jetpack Compose (Material 3) |
| არქიტექტურა | MVVM |
| ბაზა | Room |
| ნავიგაცია | Navigation Compose |
| ასინქრონულობა | Coroutines + Flow |
| დიაგრამები | Compose Canvas, ხელით |
| Min SDK | 26 (Android 8.0) |
| Compile SDK | 35 |

## ფაილების სტრუქტურა
app/src/main/java/com/saba/cargarage/
├── MainActivity.kt
├── CarApplication.kt
├── data/          (Room: Car, CarDao, CarDatabase, Repository, SeedData)
├── stats/         (სტატისტიკის გამოთვლები)
├── ui/
│   ├── CarViewModel.kt
│   ├── theme/
│   ├── navigation/
│   ├── components/   (BarChart, DonutChart)
│   └── screens/       (List, AddEdit, Detail, About, Statistics)
└── util/Format.kt

## რა მოთხოვნები შესრულდა

მენიუ — ModalNavigationDrawer-ით
ლისთი — LazyColumn, Inventory-ში
MVVM — ViewModel + Repository + Room
ბაზასთან კავშირი — Room
ახალი ფუნქცია — თვითონ დახატული Canvas დიაგრამები
README — ეს ფაილი
findViewById არსად გვხვდება, 100% Compose-ია

## როგორ გავუშვათ
bash
./gradlew assembleDebug
./gradlew installDebug

პირველივე გაშვებაზე ბაზა ავტომატურად ივსება 7 სადემო მანქანით.

## ვინ რა გააკეთა

პროექტზე ორნი ვმუშაობდით, ორ ეტაპად:

**საბა ახვლედიანი** — საბაზისო ნაწილი: Room ბაზა, MVVM, მენიუ, ლისთი და CRUD.
**ლაშა ხუციშვილი** — სტატისტიკის გვერდი და ხელით დახატული დიაგრამები.

ორივემ ცალ-ცალკე commit-ები გავაკეთეთ, მაგრამ ორივე მთლიანად ჩართული ვიყავით პროცესში.
