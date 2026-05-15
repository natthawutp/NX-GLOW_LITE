# GWH_TM_DNK
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
| 18 | DNK_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | DNK_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | DNK_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | DNK_DN_KND | NVARCHAR2(10) | N | Delivery Note Kind / 送状種類 |
| 22 | DNK_DN_NAM | NVARCHAR2(100) | N | Delivery Note Name / 送状種類名称 |
| 23 | DNK_DN_COD | NVARCHAR2(10) | Y | Delivery Note Kind COD / 配送種類コード |
| 24 | DNK_CPRD_COD | NVARCHAR2(6) | Y | Cargo product code / 貨物商品コード |
| 25 | DNK_DNPR_COD | NVARCHAR2(8) | Y | Delivery Note Kind / 送状帳票ID |
| 26 | DNK_SLPR_COD | NVARCHAR2(8) | Y | Delivery Label Kind / 荷札帳票ID |
| 27 | DNK_SLNK_KND | CHAR(2) | N | Delivery system linkage Kind / 配送システム連携区分 |
| 28 | DNK_DNDV_FLG | CHAR(1) | N | Delivery Note Division Flag / 送り状分割フラグ |
| 29 | DNK_DNNM_FLG | CHAR(1) | N | Delivery Note Numbering Flag / 送り状採番フラグ |
| 30 | DNK_CSD_KND | CHAR(2) | N | Cash on Delivery Kind / 代引区分 |
| 31 | DNK_DC_KND | CHAR(2) | Y | Delivery Class Kind / 配送級種別 |
| 32 | DNK_DVINS_FLG | CHAR(1) | N | Delivery Insurance Fee Flag/配送保険料適用フラグ |
| 33 | DNK_JP_FLG | CHAR(1) | N | JAPAN POST Usage Flag / 日本郵便使用フラグ |
| 34 | DNK_MEIT_FLG | CHAR(1) | N | MEITETSU Usage Flag / 名鉄使用フラグ |
