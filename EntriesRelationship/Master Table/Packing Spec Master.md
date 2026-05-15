# GWH_TM_PKSP
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
| 18 | PKSP_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | PKSP_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | PKSP_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | PKSP_PROD_COD | NVARCHAR2(100) | Y | Product Code / 商品コード |
| 22 | PKSP_PRDG_COD | NVARCHAR2(30) | Y | Product Group Code / 商品グループコード |
| 23 | PKSP_PIK1_AVSC_KND | CHAR(1) | N | (PIK1) Arrival Scan Kind / (PIK1) Picking Key入荷スキャン区分 |
| 24 | PKSP_PIK2_AVSC_KND | CHAR(1) | N | (PIK2) Arrival Scan Kind / (PIK2) Picking Key入荷スキャン区分 |
| 25 | PKSP_PIK3_AVSC_KND | CHAR(1) | N | (PIK3) Arrival Scan Kind / (PIK3) Picking Key入荷スキャン区分 |
| 26 | PKSP_PIK4_AVSC_KND | CHAR(1) | N | (PIK4) Arrival Scan Kind / (PIK4) Picking Key入荷スキャン区分 |
| 27 | PKSP_PIK5_AVSC_KND | CHAR(1) | N | (PIK5) Arrival Scan Kind / (PIK5) Picking Key入荷スキャン区分 |
| 28 | PKSP_PIK1_PISC_KND | CHAR(1) | N | (PIK1) Picking Scan Kind / (PIK1) Picking Key出荷Pickingスキャン区分 |
| 29 | PKSP_PIK2_PISC_KND | CHAR(1) | N | (PIK2) Picking Scan Kind / (PIK2) Picking Key出荷Pickingスキャン区分 |
| 30 | PKSP_PIK3_PISC_KND | CHAR(1) | N | (PIK3) Picking Scan Kind / (PIK3) Picking Key出荷Pickingスキャン区分 |
| 31 | PKSP_PIK4_PISC_KND | CHAR(1) | N | (PIK4) Picking Scan Kind / (PIK4) Picking Key出荷Pickingスキャン区分 |
| 32 | PKSP_PIK5_PISC_KND | CHAR(1) | N | (PIK5) Picking Scan Kind / (PIK5) Picking Key出荷Pickingスキャン区分 |
| 33 | PKSP_PIK1_PASC_KND | CHAR(1) | N | (PIK1) Packing Scan Kind / (PIK1) Picking Key出荷Packingスキャン区分 |
| 34 | PKSP_PIK2_PASC_KND | CHAR(1) | N | (PIK2) Packing Scan Kind / (PIK2) Picking Key出荷Packingスキャン区分 |
| 35 | PKSP_PIK3_PASC_KND | CHAR(1) | N | (PIK3) Packing Scan Kind / (PIK3) Picking Key出荷Packingスキャン区分 |
| 36 | PKSP_PIK4_PASC_KND | CHAR(1) | N | (PIK4) Packing Scan Kind / (PIK4) Picking Key出荷Packingスキャン区分 |
| 37 | PKSP_PIK5_PASC_KND | CHAR(1) | N | (PIK5) Packing Scan Kind / (PIK5) Picking Key出荷Packingスキャン区分 |
| 38 | PKSP_PIK1_LOSC_KND | CHAR(1) | N | (PIK1) Loading Scan Kind / (PIK1) Picking Key Loadingスキャン区分 |
| 39 | PKSP_PIK2_LOSC_KND | CHAR(1) | N | (PIK2) Loading Scan Kind / (PIK2) Picking Key Loadingスキャン区分 |
| 40 | PKSP_PIK3_LOSC_KND | CHAR(1) | N | (PIK3) Loading Scan Kind / (PIK3) Picking Key Loadingスキャン区分 |
| 41 | PKSP_PIK4_LOSC_KND | CHAR(1) | N | (PIK4) Loading Scan Kind / (PIK4) Picking Key Loadingスキャン区分 |
| 42 | PKSP_PIK5_LOSC_KND | CHAR(1) | N | (PIK5) Loading Scan Kind / (PIK5) Picking Key Loadingスキャン区分 |
| 43 | PKSP_PIK1_LCSC_KND | CHAR(1) | N | (PIK1) Location Entry Scan Kind / (PIK1) Picking Key入荷棚付スキャン区分 |
| 44 | PKSP_PIK2_LCSC_KND | CHAR(1) | N | (PIK2) Location Entry Scan Kind / (PIK2) Picking Key入荷棚付スキャン区分 |
| 45 | PKSP_PIK3_LCSC_KND | CHAR(1) | N | (PIK3) Location Entry Scan Kind / (PIK3) Picking Key入荷棚付スキャン区分 |
| 46 | PKSP_PIK4_LCSC_KND | CHAR(1) | N | (PIK4) Location Entry Scan Kind / (PIK4) Picking Key入荷棚付スキャン区分 |
| 47 | PKSP_PIK5_LCSC_KND | CHAR(1) | N | (PIK5) Location Entry Scan Kind / (PIK5) Picking Key入荷棚付スキャン区分 |
| 48 | PKSP_PIK1_RLSC_KND | CHAR(1) | N | (PIK1) Relocation Entry Scan Kind / (PIK1) Picking Keyリロケーションスキャン区分 |
| 49 | PKSP_PIK2_RLSC_KND | CHAR(1) | N | (PIK2) Relocation Entry Scan Kind / (PIK2) Picking Keyリロケーションスキャン区分 |
| 50 | PKSP_PIK3_RLSC_KND | CHAR(1) | N | (PIK3) Relocation Entry Scan Kind / (PIK3) Picking Keyリロケーションスキャン区分 |
| 51 | PKSP_PIK4_RLSC_KND | CHAR(1) | N | (PIK4) Relocation Entry Scan Kind / (PIK4) Picking Keyリロケーションスキャン区分 |
| 52 | PKSP_PIK5_RLSC_KND | CHAR(1) | N | (PIK5) Relocation Entry Scan Kind / (PIK5) Picking Keyリロケーションスキャン区分 |
