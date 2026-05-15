# GWH_TJ_YERR
#Entity #Standard #ERROR

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
| 18 | YERR_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | YERR_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | YERR_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | YERR_TSP_COD | NVARCHAR2(40) | N | True Shipper Code / 真荷主コード |
| 22 | YERR_IV_NUM | NVARCHAR2(24) | N | Invoice Number / 伝票番号 |
| 23 | YERR_SEQ_NUM | NUMBER(38) | N | Process SEQ number / 処理SEQ番号 |
| 24 | YERR_ERR_KND1 | CHAR(5) | Y | Error Kind1 / エラー区分1 |
| 25 | YERR_ERR_KND2 | CHAR(5) | Y | Error Kind2 / エラー区分2 |
| 26 | YERR_ERR_KND3 | CHAR(5) | Y | Error Kind3 / エラー区分3 |
| 27 | YERR_ERR_KND4 | CHAR(5) | Y | Error Kind4 / エラー区分4 |
| 28 | YERR_ERR_KND5 | CHAR(5) | Y | Error Kind5 / エラー区分5 |
| 29 | YERR_ERR_KND6 | CHAR(5) | Y | Error Kind6 / エラー区分6 |
| 30 | YERR_ERR_KND7 | CHAR(5) | Y | Error Kind7 / エラー区分7 |
| 31 | YERR_ERR_KND8 | CHAR(5) | Y | Error Kind8 / エラー区分8 |
| 32 | YERR_ERR_KND9 | CHAR(5) | Y | Error Kind9 / エラー区分9 |
| 33 | YERR_ERR_KND10 | CHAR(5) | Y | Error Kind10 / エラー区分10 |
| 34 | YERR_ERR_KND11 | CHAR(5) | Y | Error Kind11 / エラー区分11 |
| 35 | YERR_ERR_KND12 | CHAR(5) | Y | Error Kind12 / エラー区分12 |
| 36 | YERR_ERR_KND13 | CHAR(5) | Y | Error Kind13 / エラー区分13 |
| 37 | YERR_ERR_KND14 | CHAR(5) | Y | Error Kind14 / エラー区分14 |
| 38 | YERR_ERR_KND15 | CHAR(5) | Y | Error Kind15 / エラー区分15 |
| 39 | YERR_ERR_KND16 | CHAR(5) | Y | Error Kind16 / エラー区分16 |
| 40 | YERR_ERR_KND17 | CHAR(5) | Y | Error Kind17 / エラー区分17 |
| 41 | YERR_ERR_KND18 | CHAR(5) | Y | Error Kind18 / エラー区分18 |
| 42 | YERR_ERR_KND19 | CHAR(5) | Y | Error Kind19 / エラー区分19 |
| 43 | YERR_ERR_KND20 | CHAR(5) | Y | Error Kind20 / エラー区分20 |
| 44 | YERR_ERR_KND21 | CHAR(5) | Y | Error Kind21 / エラー区分21 |
| 45 | YERR_ERR_KND22 | CHAR(5) | Y | Error Kind22 / エラー区分22 |
| 46 | YERR_ERR_KND23 | CHAR(5) | Y | Error Kind23 / エラー区分23 |
| 47 | YERR_ERR_KND24 | CHAR(5) | Y | Error Kind24 / エラー区分24 |
| 48 | YERR_ERR_KND25 | CHAR(5) | Y | Error Kind25 / エラー区分25 |
| 49 | YERR_ERR_KND26 | CHAR(5) | Y | Error Kind26 / エラー区分26 |
| 50 | YERR_ERR_KND27 | CHAR(5) | Y | Error Kind27 / エラー区分27 |
| 51 | YERR_ERR_KND28 | CHAR(5) | Y | Error Kind28 / エラー区分28 |
| 52 | YERR_ERR_KND29 | CHAR(5) | Y | Error Kind29 / エラー区分29 |
| 53 | YERR_ERR_KND30 | CHAR(5) | Y | Error Kind30 / エラー区分30 |
| 54 | YERR_ERR_KND31 | CHAR(5) | Y | Error Kind31 / エラー区分31 |
| 55 | YERR_ERR_KND32 | CHAR(5) | Y | Error Kind32 / エラー区分32 |
| 56 | YERR_ERR_KND33 | CHAR(5) | Y | Error Kind33 / エラー区分33 |
| 57 | YERR_ERR_KND34 | CHAR(5) | Y | Error Kind34 / エラー区分34 |
| 58 | YERR_ERR_KND35 | CHAR(5) | Y | Error Kind35 / エラー区分35 |
| 59 | YERR_ERR_KND36 | CHAR(5) | Y | Error Kind36 / エラー区分36 |
| 60 | YERR_ERR_KND37 | CHAR(5) | Y | Error Kind37 / エラー区分37 |
| 61 | YERR_ERR_KND38 | CHAR(5) | Y | Error Kind38 / エラー区分38 |
| 62 | YERR_ERR_KND39 | CHAR(5) | Y | Error Kind39 / エラー区分39 |
| 63 | YERR_ERR_KND40 | CHAR(5) | Y | Error Kind40 / エラー区分40 |
| 64 | YERR_ERR_KND41 | CHAR(5) | Y | Error Kind41 / エラー区分41 |
| 65 | YERR_ERR_KND42 | CHAR(5) | Y | Error Kind42 / エラー区分42 |
