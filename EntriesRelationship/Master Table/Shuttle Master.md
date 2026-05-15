# GWH_TM_SHU
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
| 18 | SHU_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | SHU_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | SHU_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | SHU_GCUS_COD | NVARCHAR2(10) | N | (SHUTTLE)Global Customer Code / (SHUTTLE)グローバル顧客コード |
| 22 | SHU_SWHS_COD | NVARCHAR2(20) | N | (SHUTTLE)Local Customer Code / (SHUTTLE)倉庫コード |
| 23 | SHU_LCUS_COD | NVARCHAR2(10) | N | (SHUTTLE)Warehouse Code / (SHUTTLE)ローカル顧客コード |
| 24 | SHU_SDPT_COD | NVARCHAR2(10) | Y | (SHUTTLE)Department Code / (SHUTTLE)部門コード |
| 25 | SHU_FILE_NAM | NVARCHAR2(40) | N | Linkage File Name / 連携ファイル名 |
| 26 | SHU_LNK_FLG | CHAR(1) | N | Linkage Flag / 連携フラグ |
| 27 | SHU_RMKS | NVARCHAR2(200) | Y | Remarks / 備考 |
| 28 | SHU_INTR_FLG | CHAR(1) | N | Send In-Transit Flag/輸送中在庫送信フラグ |
| 29 | SHU_PRD_LNK_FLG | CHAR(1) | N | Product master Linkage Flag / 商品マスタ連携フラグ |
| 30 | SHU_SRL_LNK_FLG | CHAR(1) | N | Serial number Linkage Flag / シリアルNo連携フラグ |
| 31 | SHU_TRK_LNK_FLG | CHAR(1) | N | Tracking number Linkage Flag / トラッキングNo.連携フラグ |
| 32 | SHU_BL_CNV_FLG | CHAR(1) | N | BL number Convert Flag / 輸送管理番号変換フラグ |
