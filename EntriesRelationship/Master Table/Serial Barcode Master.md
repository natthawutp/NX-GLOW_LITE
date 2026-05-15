# GWH_TM_SBAR
#Entity #Standard #MASTER

| # | Column Name | Data Type | Nullable | Description |
|---|------------|-----------|----------|-------------|
| 1 | DEL_FLG | CHAR(1) | N | Delete Flag / 論理削除フラグ |
| 2 | CRT_YMD | CHAR(8) | N | Create Date (UTC) / 作成日(標準時) |
| 3 | CRT_TIM | CHAR(6) | N | Create HMS (UTC) / 作成時分秒(標準時) |
| 4 | CRT_TMID | NVARCHAR2(200) | N | Create Term Id / 作成端末ID |
| 5 | CRT_USER | NVARCHAR2(100) | N | Create User Id / 作成ユーザID |
| 6 | CRT_PGM | NVARCHAR2(60) | N | Create Program Id / 作成プログラムID |
| 7 | CRT_TM_ZONE | NVARCHAR2(6) | N | Create User Time Zone / 作成ユーザタイムゾーン |
| 8 | CRT_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時） |
| 9 | CRT_L_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| 10 | UPD_YMD | CHAR(8) | N | Update Date (UTC) / 更新日(標準時) |
| 11 | UPD_TIM | CHAR(6) | N | Update HMS (UTC) / 更新時分秒(標準時) |
| 12 | UPD_TMID | NVARCHAR2(200) | N | Update Terminal ID / 更新端末ID |
| 13 | UPD_USER | NVARCHAR2(100) | N | Update User ID / 更新ユーザID |
| 14 | UPD_PGM | NVARCHAR2(60) | N | Update Program ID / 更新プログラムID |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N | Update User Time Zone / 更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| 18 | SBAR_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | SBAR_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | SBAR_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | SBAR_TYPE_KND | NVARCHAR2(4) | N | Barcode Type Kind / バーコード種別 |
| 22 | SBAR_SPT_KND | NVARCHAR2(2) | Y | Split Kind / 可変固定長区分 |
| 23 | SBAR_SPT_VAL | NVARCHAR2(6) | Y | Product Seperator (Variable) / 区切り文字(可変) |
| 24 | SBAR_PROD_POS | NUMBER(3) | Y | Product Position / 商品位置 |
| 25 | SBAR_PROD_LEN | NUMBER(3) | Y | Product Length / 商品桁数 |
| 26 | SBAR_PROD_TRIM_FLG | CHAR(1) | N | Product Trim Flag / 商品トリムフラグ |
| 27 | SBAR_PROD_AI | NVARCHAR2(8) | Y | Product AI (GS1-128) / 商品AI (GS1-128) |
| 28 | SBAR_PROD_IT_FLG | CHAR(1) | N | Product Indicator Trim Flag (GS1-128) / 商品インジケータトリムフラグ (GS1-128) |
| 29 | SBAR_DMG_POS | NUMBER(3) | Y | Damaged/Hold Position / ダメージHoldフラグ位置 |
| 30 | SBAR_DMG_LEN | NUMBER(3) | Y | Damaged/Hold Length / ダメージHoldフラグ桁数 |
| 31 | SBAR_DMG_TRIM_FLG | CHAR(1) | N | Damaged/Hold Trim Flag / ダメージHoldフラグトリムフラグ |
| 32 | SBAR_DMG_AI | NVARCHAR2(8) | Y | Damaged/Hold AI (GS1-128) / ダメージHoldフラグAI (GS1-128) |
| 33 | SBAR_SBIV_POS | NUMBER(3) | Y | Sub Inventory code Position / 等級位置 |
| 34 | SBAR_SBIV_LEN | NUMBER(3) | Y | Sub Inventory code Length / 等級桁数 |
| 35 | SBAR_SBIV_TRIM_FLG | CHAR(1) | N | Sub Inventory code Trim Flag / 等級トリムフラグ |
| 36 | SBAR_SBIV_AI | NVARCHAR2(8) | Y | Sub Inventory code AI (GS1-128) / 等級AI (GS1-128) |
| 37 | SBAR_QTY_POS | NUMBER(3) | Y | Quantity Position / 数量位置 |
| 38 | SBAR_QTY_LEN | NUMBER(3) | Y | Quantity Length / 数量桁数 |
| 39 | SBAR_QTY_TRIM_FLG | CHAR(1) | N | Quantity Trim Flag / 数量トリムフラグ |
| 40 | SBAR_QTY_AI | NVARCHAR2(8) | Y | Quantity AI (GS1-128) / 数量AI (GS1-128) |
| 41 | SBAR_PIK1_POS | NUMBER(3) | Y | PICKING KEY 1 Position / ピッキングキー1 位置 |
| 42 | SBAR_PIK1_LEN | NUMBER(3) | Y | PICKING KEY 1 Length / ピッキングキー1 桁数 |
| 43 | SBAR_PIK1_TRIM_FLG | CHAR(1) | N | PICKING KEY 1 Trim Flag / ピッキングキー1 トリムフラグ |
| 44 | SBAR_PIK1_AI | NVARCHAR2(8) | Y | PICKING KEY 1 AI (GS1-128) / ピッキングキー1 AI (GS1-128) |
| 45 | SBAR_PIK2_POS | NUMBER(3) | Y | PICKING KEY 2 Position / ピッキングキー2 位置 |
| 46 | SBAR_PIK2_LEN | NUMBER(3) | Y | PICKING KEY 2 Length / ピッキングキー2 桁数 |
| 47 | SBAR_PIK2_TRIM_FLG | CHAR(1) | N | PICKING KEY 2 Trim Flag / ピッキングキー2 トリムフラグ |
| 48 | SBAR_PIK2_AI | NVARCHAR2(8) | Y | PICKING KEY 2 AI (GS1-128) / ピッキングキー2 AI (GS1-128) |
| 49 | SBAR_PIK3_POS | NUMBER(3) | Y | PICKING KEY 3 Position / ピッキングキー3 位置 |
| 50 | SBAR_PIK3_LEN | NUMBER(3) | Y | PICKING KEY 3 Length / ピッキングキー3 桁数 |
| 51 | SBAR_PIK3_TRIM_FLG | CHAR(1) | N | PICKING KEY 1 Trim Flag / ピッキングキー3 トリムフラグ |
| 52 | SBAR_PIK3_AI | NVARCHAR2(8) | Y | PICKING KEY 3 AI (GS1-128) / ピッキングキー3 AI (GS1-128) |
| 53 | SBAR_PIK4_POS | NUMBER(3) | Y | PICKING KEY 4 Position / ピッキングキー4 位置 |
| 54 | SBAR_PIK4_LEN | NUMBER(3) | Y | PICKING KEY 4 Length / ピッキングキー4 桁数 |
| 55 | SBAR_PIK4_TRIM_FLG | CHAR(1) | N | PICKING KEY 4 Trim Flag / ピッキングキー4 トリムフラグ |
| 56 | SBAR_PIK4_AI | NVARCHAR2(8) | Y | PICKING KEY 4 AI (GS1-128) / ピッキングキー4 AI (GS1-128) |
| 57 | SBAR_PIK5_POS | NUMBER(3) | Y | PICKING KEY 5 Position / ピッキングキー5 位置 |
| 58 | SBAR_PIK5_LEN | NUMBER(3) | Y | PICKING KEY 5 Length / ピッキングキー5 桁数 |
| 59 | SBAR_PIK5_TRIM_FLG | CHAR(1) | N | PICKING KEY 5 Trim Flag / ピッキングキー5 トリムフラグ |
| 60 | SBAR_PIK5_AI | NVARCHAR2(8) | Y | PICKING KEY 5 AI (GS1-128) / ピッキングキー5 AI (GS1-128) |
| 61 | SBAR_PIK6_POS | NUMBER(3) | Y | PICKING KEY 6 Position / ピッキングキー6 位置 |
| 62 | SBAR_PIK6_LEN | NUMBER(3) | Y | PICKING KEY 6 Length / ピッキングキー6 桁数 |
| 63 | SBAR_PIK6_TRIM_FLG | CHAR(1) | N | PICKING KEY 6 Trim Flag / ピッキングキー6 トリムフラグ |
| 64 | SBAR_PIK6_AI | NVARCHAR2(8) | Y | PICKING KEY 6 AI (GS1-128) / ピッキングキー6 AI (GS1-128) |
| 65 | SBAR_PIK7_POS | NUMBER(3) | Y | PICKING KEY 7 Position / ピッキングキー7 位置 |
| 66 | SBAR_PIK7_LEN | NUMBER(3) | Y | PICKING KEY 7 Length / ピッキングキー7 桁数 |
| 67 | SBAR_PIK7_TRIM_FLG | CHAR(1) | N | PICKING KEY 7 Trim Flag / ピッキングキー7 トリムフラグ |
| 68 | SBAR_PIK7_AI | NVARCHAR2(8) | Y | PICKING KEY 7 AI (GS1-128) / ピッキングキー7 AI (GS1-128) |
